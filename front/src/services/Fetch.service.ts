import fetch from 'isomorphic-unfetch';
import Cookies from 'universal-cookie';
import { authCookieName } from './JWT.service';
type FetchType = {
  url: string;
  data?: object;
  type?: 'POST' | 'GET';
  headers?: { [key: string]: string };
};
class FetchService {
  public async isofetch<T>({ url, data, type = 'GET', headers }: FetchType): Promise<T | null> {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${url}`, {
        body: data ? JSON.stringify({ ...data }) : undefined,
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          ...headers
        },
        method: type
      });
      const parsedData = await response.text();
      return parsedData ? (JSON.parse(parsedData) as T) : null;
    } catch (error) {
      throw error;
    }
  }

  public isofetchAuthed<T>({ url, data, type, headers }: FetchType): Promise<T | null> {
    const cookies = new Cookies();
    const token = cookies.get(authCookieName);
    return this.isofetch<T>({
      url,
      data,
      type,
      headers: { ...headers, Authorization: token }
    });
  }
}

export default new FetchService();
