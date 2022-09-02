import Link from 'next/link';
import * as React from 'react';
import { NextPageContext } from 'next';
import { useForm, Controller } from 'react-hook-form';
import PageContent from '../components/PageContent';
import { ActionType, useAuth } from '../services/Auth.context';

import FetchService from '../services/Fetch.service';
import TokenService from '../services/JWT.service';

import { ILoginIn } from '../types/auth.types';
import { SchemaOf } from 'yup';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { FormInputWithController } from '../components/FormInput';
import { useRouter } from 'next/router';

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
  const tokenService = new TokenService();
  const [authState, authDispatch] = useAuth();
  const router = useRouter();
  // Log user out when they are directed to the /l=t URL - caught in the getInitialProps at the
  // bottom of the page
  React.useEffect(() => {
    if (props.action && props.action == 'logout') {
      authDispatch({
        type: ActionType.RemoveDetails
      });

      tokenService.deleteToken();

      router.push('/');
    }
  }, []);

  const {
    handleSubmit,
    control
  } = useForm<LoginInput>({
    resolver: yupResolver(schema),
    defaultValues: {
      email: '',
      password: ''
    }
  });
  const onLogIn = React.useCallback(async (values: ILoginIn) => {
    const {
      token,
      hasError: hasGlobalErrors,
      errors,
      ...userData
    } = await FetchService.isofetch<LoginResponce<boolean>>({
      url: '/auth/authenticate',
      data: {
        email: values.email,
        password: values.password
      },
      type: 'POST'
    });
    if (!hasGlobalErrors) {
      // save token in cookie for subsequent requests
      const tokenService = new TokenService();
      tokenService.saveToken(token);

      authDispatch({
        type: ActionType.SetDetails,
        payload: userData
      });

      // router.push('/dashboard');
    } else {
      alert(errors);
    }
  }, []);
  return (
    <PageContent>
      <div className="flex flex-col mx-auto">
        {!authState && <form onSubmit={handleSubmit(onLogIn)} className="flex flex-col space-y-4">
          <FormInputWithController control={control} name="email" type="email" labelName="Email" />
          <FormInputWithController
            control={control}
            name="password"
            type="password"
            labelName="password"
          />

          <button
            type="submit"
            className="border text-sm text-blue-600 hover:opacity-70 active:scale-90 px-3 py-2 rounded-md border-blue-400"
          >
            Submit
          </button>
        </form>}
    {authState && <p className='text-blue-900'>Loged in successfully.</p>}
      </div>
    </PageContent>
  );
}

Home.getInitialProps = async (ctx: NextPageContext) => {
  if (ctx.query && ctx.query.l === 't') {
    return { action: 'logout' };
  }
  const authService = new TokenService()
  await authService.authenticateTokenSsr(ctx)
  return {
    action: undefined
  };
};

export default Home;
