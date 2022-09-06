import { useCallback, useState } from "react";
import { QueryComponentProps } from "../../pages/dashboard/charts";
import FetchService from "../../services/Fetch.service";
import extractDate from "../../utils/extractDate";
import getLineChartReadable, { Res } from "../../utils/getChartReadable";
import humanReadableTime from "../../utils/humanReadableTime";
import LineChart from "../LineChart";


type ResponseType = {
    data: { count: number; date: string }[]
}

export default function OnlineUser({ startDate, endDate }: QueryComponentProps): JSX.Element {
    const [response, setResponse] = useState<Res>(null)
    const onSubmit = useCallback(async () => {
        const {
            data
        } = await FetchService.isofetch<ResponseType>({
            url: '/query/online',
            data: {
                start: extractDate(startDate),
                end: extractDate(endDate)
            },
            type: 'POST'
        });
        setResponse(getLineChartReadable({ data, start: startDate, end: endDate }))
    }, [startDate, endDate])
    return (
        <>
            <button type="button" value="query" onClick={onSubmit} >Submit</button>
            {response && <LineChart data={response.data} labels={response.labels.map(date => humanReadableTime(date))} />}
        </>
    )
}