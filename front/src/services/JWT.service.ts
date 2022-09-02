import { NextPageContext } from 'next/types';

import Cookies from 'universal-cookie';

import FetchService from '../services/Fetch.service';
import NavService from '../services/Nav.service';

type ValidateAuthResponce = {
  success: true;
};
export const authCookieName = process.env.NEXT_PUBLIC_AUTH_COOKIE_NAME;
class TokenService {
  public saveToken(token: string) {
    const cookies = new Cookies();
    cookies.set(authCookieName, token, { path: '/' });
    return Promise.resolve();
  }

  public deleteToken() {
    const cookies = new Cookies();
    cookies.remove(authCookieName, { path: '/' });
    return;
  }

  public checkAuthToken(token: string, ssr: boolean): Promise<ValidateAuthResponce | null> {
    return FetchService.isofetchAuthed<ValidateAuthResponce>({
      url: `/auth/validate`,
      type: 'POST',
      headers: {
        [authCookieName]: token,
      },
      ssr,
    });
  }

  public async authenticateTokenSsr(ctx: NextPageContext) {
    const ssr = ctx.req ? true : false;
    const cookies = new Cookies(ssr ? ctx.req?.headers.cookie : null);
    const token = cookies.get(authCookieName);
    const response = await this.checkAuthToken(token, ssr);
    if (!response?.success) {
      const navService = new NavService();
      this.deleteToken();
      navService.redirectUser('/?l=t', ctx);
    }
  }
}

export default TokenService;
