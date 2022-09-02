import Head from 'next/head';
import * as React from 'react';
import { useRouter } from 'next/router';

import { ActionType, useAuth } from '../services/Auth.context';
import JWTService from '../services/JWT.service';
import Link from 'next/link';

interface IProps {}

function Header(props: IProps) {
  const tokenService = new JWTService();
  const [auth, authDispatch] = useAuth();
  const router = useRouter();
  const onLogOut = React.useCallback(() => {
    authDispatch({
      type: ActionType.RemoveDetails
    });
    tokenService.deleteToken();
    router.push('/');
  }, []);
  return (
    <div className="pt-4 px-5 bg-gray-400">
      <div className="text-center w-full m-0">
        <Head>
          <title>Customer Data Platform</title>
          <meta name="viewport" content="initial-scale=1.0, width=device-width" />
        </Head>
        {auth ? (
          <>
            <div className="text-right">
              <p>Logged in with user: {auth.email}</p>
              <button className="text-blue-800" onClick={onLogOut}>
                Log out
              </button>
            </div>
            <div className="text-left">
              <Link href="/dashboard">
                <a>Dashboard</a>
              </Link>
            </div>
          </>
        ) : null}
        <h1 className="text-3xl">Customer Data Platform</h1>
      </div>
    </div>
  );
}

export default Header;
