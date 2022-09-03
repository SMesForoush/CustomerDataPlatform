import { QueryComponentProps } from "../../pages/dashboard/charts";
import * as yup from 'yup'
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { FormInputWithController } from "../FormInput";
import { useCallback, useState } from "react";
import FetchService from "../../services/Fetch.service";
import extractDate from "../../utils/extractDate";
import LineChart from "../LineChart";
import humanReadableTime from "../../utils/humanReadableTime";

type QueryInput = {
    course: string;
}
const schema: yup.SchemaOf<QueryInput> = yup.object().shape({
    course: yup.string().required().min(5),
});


type ResponseType = {
    data: { count: number; date: string }[]
}

export default function OnlineUsersForCource({ startDate, endDate }: QueryComponentProps): JSX.Element {
    const [response, setResponse] = useState<ResponseType['data']>(null)
    const {
        control,
        watch,
    } = useForm<QueryInput>(
        {
            resolver: yupResolver(schema),
            defaultValues: {
                course: "",
            }
        }
    )
    const watchCourse = watch('course')
    const onSubmit = useCallback(async () => {
        const {
            data
        } = await FetchService.isofetch<ResponseType>({
            url: '/query/online/course',
            data: {
                start: extractDate(startDate),
                end: extractDate(endDate),
                course: watchCourse,
            },
            type: 'POST'
        });
        setResponse(data)
    }, [startDate, endDate, watchCourse])

    return (
        <>
            <FormInputWithController control={control} name="course" labelName="Course Id" />
            {watchCourse &&
                <button type="button" value="query" onClick={onSubmit}>Submit </button>
            }
            {response && <LineChart data={response.map(click => click.count)} labels={response.map(click => humanReadableTime(click.date))} />}
        </>
    )
}