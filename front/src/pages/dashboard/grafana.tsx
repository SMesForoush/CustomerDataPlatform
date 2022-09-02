import * as React from 'react';
import { NextPageContext } from 'next';
import PageContent from '../../components/PageContent';

import TokenService from '../../services/JWT.service';

import { SchemaOf } from 'yup';
import * as yup from 'yup';
import ErrorPage from '../../components/ErrorPage';
import { Dashboard } from '../../components/dashboard';
import WithAuth from '../../components/WithAuth';
import Link from 'next/link';

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
    return (
        <div className="">
            <Link href="/dashboard" >
                <a className='text-blue-800'>
                    Back to dashboard
                </a>
            </Link>
            <WithAuth withAuth={false}>
                <ErrorPage message="Login Required Page" />
            </WithAuth>
            <WithAuth >
                <iframe width="100%" className='h-screen' height="100%" src={`${process.env.NEXT_PUBLIC_GRAFANA_URL}`} />
            </WithAuth>
        </div>
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
