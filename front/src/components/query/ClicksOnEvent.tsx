import { useCallback, useState } from "react";
import { QueryComponentProps } from "../../pages/dashboard/charts";
import FetchService from "../../services/Fetch.service";
import extractDate from "../../utils/extractDate";
import humanReadableTime from "../../utils/humanReadableTime";
import { PieChart } from "../PieChart";


type ResponseType = {
    data: { count: number; event: string }[]
}
export default function ClicksOnEvent(date: QueryComponentProps): JSX.Element {
    const [response, setResponse] = useState<ResponseType['data']>(null)
    const onSubmit = useCallback(async () => {
        const {
            data
        } = await FetchService.isofetch<ResponseType>({
            url: '/query/event/clicks',
            data: date,
            type: 'POST'
        });
        setResponse(data)
    }, [date])
    return (
        <>
            <button type="button" value="query" onClick={onSubmit} >Submit</button>
            {response && <PieChart data={response.map(click => click.count)} labels={response.map(click => click.event)} />}
        </>
    )
}