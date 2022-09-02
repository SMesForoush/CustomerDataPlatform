import * as React from 'react';
import { NextPageContext } from 'next';

import TokenService from '../../services/JWT.service';

import PageContent from '../../components/PageContent';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    ChartOptions,
    ChartData,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import LineChart from '../../components/LineChart';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);


const labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
const data = labels.map(() => Math.random() * 100);


interface IProps {
    action: string;
}

function Home(props: IProps) {
    const x = [1, 2, 3, 4]
    return (
        <PageContent>
            <LineChart data={data} labels={labels} />;
        </PageContent>

    );
}

Home.getInitialProps = async (ctx: NextPageContext) => {
    const authService = new TokenService();
    // await authService.authenticateTokenSsr(ctx);
    return {
        action: undefined
    };
};

export default Home;
