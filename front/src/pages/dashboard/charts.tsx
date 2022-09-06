import * as React from 'react';
import { NextPageContext } from 'next';

import TokenService from '../../services/JWT.service';
import * as yup from 'yup'
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
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { FormInputWithController } from '../../components/FormInput';
import ClicksOnEvent from '../../components/query/ClicksOnEvent';
import OnlineUser from '../../components/query/OnlineUsers';
import VideoPlays from '../../components/query/VideoPlays';
import OnlineUsersForCource from '../../components/query/OnlineUsersForCourse';
import WithAuth from '../../components/WithAuth';
import ErrorPage from '../../components/ErrorPage';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

interface IProps {
    action: string;
}

enum QueryType {
    onlineUsers = 'online user',
    onlineUsersForCource = 'online users for course',
    clicksOnEvent = 'clicks on each event',
    videoPlay = 'video play time'
}

type QueryInput = {
    startDate: string;
    endDate: string;
    type: QueryType
}

export type QueryComponentProps = Omit<QueryInput, 'type'>
const QueryTypeMapperComponent: Record<QueryType, React.ComponentType<QueryComponentProps>> = {
    [QueryType.clicksOnEvent]: ClicksOnEvent,
    [QueryType.onlineUsers]: OnlineUser,
    [QueryType.videoPlay]: VideoPlays,
    [QueryType.onlineUsersForCource]: OnlineUsersForCource
}

const schema: yup.SchemaOf<QueryInput> = yup.object().shape({
    startDate: yup.string().required().min(5),
    endDate: yup.string().required().min(5),
    type: yup.mixed().oneOf((Object.values(QueryType))).required()
});

function Home(props: IProps) {
    const { control, watch } = useForm<QueryInput>(
        {
            resolver: yupResolver(schema),
            defaultValues: {
                startDate: '',
                endDate: "",
                type: undefined,
            }
        }
    )
    const watchEndDate = watch('endDate')
    const watchStartDate = watch('startDate')
    const watchType = watch('type')
    
    const Component = QueryTypeMapperComponent[watchType]
    return (
        <PageContent>

            <WithAuth withAuth={false}>
                <ErrorPage message="Login Required Page" />
            </WithAuth>
            <WithAuth>
                <>
                    <h2>Complete Form To See Charts.</h2>
                    <form className="flex flex-col space-y-4">

                        <FormInputWithController name='startDate' labelName='Start Date' control={control} type="datetime-local" />
                        {watchStartDate && <FormInputWithController name='endDate' labelName='End Date' control={control} type="datetime-local" />}
                        {watchEndDate && <FormInputWithController name='type' labelName='Query Type' control={control} render={(field) => {
                            return (
                                <select {...field}>
                                    <option key={""} value="">not selected</option>
                                    {Object.values(QueryType).map(value => {
                                        return <option key={value} value={value}>{value}</option>
                                    })}
                                </select>
                            )
                        }} />}
                        {watchType && Component && <Component startDate={watchStartDate} endDate={watchEndDate} />}
                    </form>
                </>
            </WithAuth>
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
