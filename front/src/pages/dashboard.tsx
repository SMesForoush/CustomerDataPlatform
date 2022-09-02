import * as React from 'react';
import { NextPageContext } from 'next';
import PageContent from '../components/PageContent';

import TokenService from '../services/JWT.service';

import { SchemaOf } from 'yup';
import * as yup from 'yup';
import ErrorPage from '../components/ErrorPage';
import { Dashboard } from '../components/dashboard';
import WithAuth from '../components/WithAuth';

interface IProps {
  action: string;
}


function Home(props: IProps) {
  return (
    <PageContent>
      <div className="flex flex-col mx-auto">
        <WithAuth withAuth={false}>
          <ErrorPage message="Login Required Page" />
        </WithAuth>
        <WithAuth >
          <Dashboard />
        </WithAuth>
      </div>
    </PageContent>
  );
}

Home.getInitialProps = async (ctx: NextPageContext) => {
  const authService = new TokenService();
  await authService.authenticateTokenSsr(ctx);
  return {
    action: undefined
  };
};

export default Home;
