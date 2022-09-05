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
import { DefaultValues, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { FormInputWithController } from '../../components/FormInput';
import ClicksOnEvent from '../../components/query/ClicksOnEvent';
import OnlineUser from '../../components/query/OnlineUsers';
import VideoPlays from '../../components/query/VideoPlays';
import OnlineUsersForCource from '../../components/query/OnlineUsersForCourse';
import WithAuth from '../../components/WithAuth';
import ErrorPage from '../../components/ErrorPage';
import moment from 'moment';
import DateInput from '../../components/DateInput';

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

export type QueryInput = {
    startHour: number;
    startMinute: number;
    startYear: number;
    startMonth: number;
    startDay: number;
    endHour: number;
    endMinute: number;
    endYear: number;
    endMonth: number;
    endDay: number;
    type: QueryType
}

export type QueryComponentProps = Omit<QueryInput, 'type'>
export type ResponseType = {
    data: { count: number; date: number }[]
}
const QueryTypeMapperComponent: Record<QueryType, React.ComponentType<QueryComponentProps>> = {
    [QueryType.clicksOnEvent]: ClicksOnEvent,
    [QueryType.onlineUsers]: OnlineUser,
    [QueryType.videoPlay]: VideoPlays,
    [QueryType.onlineUsersForCource]: OnlineUsersForCource
}

const schema: yup.SchemaOf<QueryInput> = yup.object().shape({
    startHour: yup.number().min(0).max(23),
    startMinute: yup.number().min(0).max(59),
    startYear: yup.number().min(2020).max(moment().year()).required(),
    startMonth: yup.number().min(1).max(12),
    startDay: yup.number().min(1).max(30),
    endHour: yup.number().min(0).max(24),
    endMinute: yup.number().min(0).max(59),
    endYear: yup.number().min(2020).max(moment().year()).required(),
    endMonth: yup.number().min(1).max(59),
    endDay: yup.number().min(1).max(30),
    type: yup.mixed().oneOf((Object.values(QueryType))).required()
});

const defaultValues: DefaultValues<QueryInput> = {
    startDay: undefined,
    startHour: undefined,
    startMinute: undefined,
    startMonth: undefined,
    startYear: moment().year() - 1,
    endDay: undefined,
    endHour: undefined,
    endMinute: undefined,
    endMonth: undefined,
    endYear: moment().year(),
    type: undefined
}

function Home(props: IProps) {
    const { control, watch, } = useForm<QueryInput>(
        {
            resolver: yupResolver(schema),
            defaultValues
        }
    )
    const watchType = watch('type')

    const [startYear,
        startMonth,
        startDay,
        startHour,
        startMinute,
        endYear,
        endMonth,
        endDay,
        endHour,
        endMinute
    ] = watch(
        [
            "startYear",
            "startMonth",
            "startDay",
            "startHour",
            "startMinute",
            "endYear",
            "endMonth",
            "endDay",
            "endHour",
            "endMinute"
        ])
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
                        <DateInput control={control} watch={watch} />
                        <FormInputWithController name='type' labelName='Query Type' control={control} render={(field) => {
                            return (
                                <select {...field}>
                                    <option key={""} value="">not selected</option>
                                    {Object.values(QueryType).map(value => {
                                        return <option key={value} value={value}>{value}</option>
                                    })}
                                </select>
                            )
                        }} />
                        {watchType && Component && <Component
                            startDay={startDay}
                            startHour={startHour}
                            startMinute={startMinute}
                            startMonth={startMonth}
                            startYear={startYear}
                            endDay={endDay}
                            endHour={endHour}
                            endMinute={endMinute}
                            endMonth={endMonth}
                            endYear={endYear}
                        />}
                    </form>
                </>
            </WithAuth>
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
