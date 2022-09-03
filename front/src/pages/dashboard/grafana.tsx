import * as React from 'react';
import { NextPageContext } from 'next';

import TokenService from '../../services/JWT.service';

import ErrorPage from '../../components/ErrorPage';
import WithAuth from '../../components/WithAuth';
import Link from 'next/link';

interface IProps {
    action: string;
}

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
