import Link from 'next/link';
import * as React from 'react';
import { NextPageContext } from 'next';
import { useForm, Controller } from 'react-hook-form';
import PageContent from '../components/PageContent';
import { ActionType, useAuth } from '../services/Auth.context';

import TokenService from '../services/JWT.service';

import { SchemaOf } from 'yup';
import * as yup from 'yup';
import ErrorPage from '../components/ErrorPage';
import { Dashboard } from '../components/dashboard';

interface IProps {
  action: string;
}

type LoginResponce<T extends boolean> = {
  token: T extends true ? undefined : string;
  email: T extends true ? undefined : string;
  name: T extends true ? undefined : string;
  hasError: T;
  errors?: T extends true ? { [key: string]: string } : undefined;
};
type LoginInput = {
  email: string;
  password: string;
};

const schema: SchemaOf<LoginInput> = yup.object().shape({
  email: yup.string().required().min(5),
  password: yup.string().required().min(1)
});

function Home(props: IProps) {
  const [authState] = useAuth();

  return (
    <PageContent>
      <div className="flex flex-col mx-auto">
        {!authState && <ErrorPage message="Login Required Page" />}
        {authState && <Dashboard />}
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
