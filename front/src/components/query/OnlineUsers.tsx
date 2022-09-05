import { useCallback, useState } from "react";
import { QueryComponentProps, ResponseType } from "../../pages/dashboard/charts";
import FetchService from "../../services/Fetch.service";
import getLineChartReadable, { Res } from "../../utils/getChartReadable";
import LineChart from "../LineChart";

export default function OnlineUser(date: QueryComponentProps): JSX.Element {
    const [response, setResponse] = useState<Res>(null)
    const onSubmit = useCallback(async () => {
        const {
            data
        } = await FetchService.isofetch<ResponseType>({
            url: '/query/online',
            data: date,
            type: 'POST'
        });
        setResponse(getLineChartReadable({ data, date }))
    }, [date])
    return (
        <>
            <button type="button" value="query" onClick={onSubmit} >Submit</button>
            {response && <LineChart data={response.data} labels={response.labels} />}
        </>
    )
}