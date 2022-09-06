import { InputHTMLAttributes } from "react";
import { Control, FieldPath } from "react-hook-form"
import { FormInputWithController } from "../FormInput";

type Props<T, TName extends FieldPath<T>> = InputHTMLAttributes<HTMLInputElement> & {
    control: Control<T>;
    name: TName;
    label: string;
    min: number;
    max: number;
    disableEmpty?: boolean
}
function range(size: number, startAt = 0) {
    return [...Array.from(Array(size).keys())].map(i => i + startAt);
}
export default function OptionNumberInput<T, TName extends FieldPath<T>>({ control, name, min, max, label, disableEmpty, ...props }: Props<T, TName>): JSX.Element {
    return (
        <FormInputWithController name={name} labelName={label} control={control} render={(field) => {
            return (
                <select {...(field as any)} {...props} >
                    {!disableEmpty && <option key={""} value="">not selected</option>}
                    {range(max - min + 1, min).map(value => {
                        return <option key={value} value={value}>{value}</option>
                    })}
                </select>
            )
        }} />
    )

}