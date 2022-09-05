import { QueryComponentProps, ResponseType } from "../../pages/dashboard/charts";
import * as yup from 'yup'
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { FormInputWithController } from "../FormInput";
import { useCallback, useState } from "react";
import FetchService from "../../services/Fetch.service";
import LineChart from "../LineChart";
import getLineChartReadable, { Res } from "../../utils/getChartReadable";

type QueryInput = {
    course: string;
}
const schema: yup.SchemaOf<QueryInput> = yup.object().shape({
    course: yup.string().required().min(5),
});



export default function OnlineUsersForCource(date: QueryComponentProps): JSX.Element {
    const [response, setResponse] = useState<Res>(null)
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
                ...date,
                course: watchCourse,
            },
            type: 'POST'
        });
        setResponse(getLineChartReadable({ data, date }))
    }, [date, watchCourse])

    return (
        <>
            <FormInputWithController control={control} name="course" labelName="Course Id" />
            {watchCourse &&
                <button type="button" value="query" onClick={onSubmit}>Submit </button>
            }
            {response && <LineChart data={response.data} labels={response.labels} />}
        </>
    )
}