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
  hasGlobalErrors: T;
  globalError: T extends true ? string : undefined;
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
  const router = useRouter()
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
    formState: { errors },
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
    const { token, hasGlobalErrors, globalError, ...userData } = await FetchService.isofetch<
      LoginResponce<boolean>
    >({
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

      router.push('/dashboard');
    } else {
      alert(globalError);
    }
  }, []);
  return (
    <PageContent>
      <div className='flex flex-col mx-auto'>
        <form onSubmit={handleSubmit(onLogIn)} className="flex flex-col space-y-4">
          <FormInputWithController control={control} name="email" type="email" labelName="Email" />
          <FormInputWithController
            control={control}
            name="password"
            type="password"
            labelName="password"
          />

          <button type="submit" className='border text-sm text-blue-600 hover:opacity-70 active:scale-90 px-3 py-2 rounded-md border-blue-400'>
            Submit
          </button>
        </form>

        <div>
          Click{' '}
          <Link href="/about">
            <a className='text-blue-400'>here</a>
          </Link>{' '}
          to read more
        </div>
        <div>
          Click{' '}
          <Link href="/register">
            <a className='text-blue-400'>here</a>
          </Link>{' '}
          to register
        </div>
      </div>
    </PageContent>
  );
}

Home.getInitialProps = async (ctx: NextPageContext) => {
  if (ctx.query && ctx.query.l == 't') {
    return { action: 'logout' };
  }
  return {};
};

export default Home;
