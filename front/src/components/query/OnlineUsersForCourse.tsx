import { QueryComponentProps } from "../../pages/dashboard/charts";
import * as yup from 'yup'
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { FormInputWithController } from "../FormInput";

type QueryInput = {
    course: string;
}

export type QueryComponentProps = Omit<QueryInput, 'type'>

const schema: yup.SchemaOf<QueryInput> = yup.object().shape({
    course: yup.string().required().min(5),
});

export default function OnlineUsersForCource({ startDate, endDate }: QueryComponentProps): JSX.Element {
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
    return (
        <>
            <FormInputWithController control={control} name="course" labelName="Course Id" />
            {watchCourse &&

                <button type="button" value="query" >Submit </button>
            }
        </>
    )
}