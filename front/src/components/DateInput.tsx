import moment from "moment";
import { Control, useController, UseFormWatch } from "react-hook-form"
import { QueryInput } from "../pages/dashboard/charts";
import { FormInputWithController } from "./FormInput"
import OptionNumberInput from "./input/optionNumberInput";

type Props = {
    control: Control<QueryInput>;
    watch: UseFormWatch<QueryInput>;
    // startName: string;
    // endName: string;
}

export default function DateInput({ control, watch }: Props): JSX.Element {
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

    const hourDisable = !!(startMinute)
    const dayDisable = !!(startHour || hourDisable)
    const monthDisable = !!(startDay || dayDisable)
    const yearDisable = !!(startMonth || monthDisable)

    const visibleMonth = !!(startYear && endYear)
    const visibleDay = !!(visibleMonth && startMonth && endMonth)
    const visibleHour = !!(visibleDay && startDay && endDay)
    const visibleMinute = !!(visibleHour && startHour && endHour)
    return (
        <>
            <div className="flex space-x-2">
                <OptionNumberInput name="startYear" label="Year" control={control} min={2020} max={moment().year()} disabled={yearDisable} disableEmpty />
                {visibleMonth && <OptionNumberInput name="startMonth" label="Month" control={control} min={1} max={12} disabled={monthDisable} />}
                {visibleDay && <OptionNumberInput name="startDay" label="Day" control={control} min={1} max={30} disabled={dayDisable} />}
                {visibleHour && <OptionNumberInput name="startHour" label="Hour" control={control} min={0} max={23} disabled={hourDisable} />}
                {visibleMinute && <OptionNumberInput name="startMinute" label="Minute" control={control} min={0} max={59} />}
            </div>

            <div className="flex space-x-2">
                <OptionNumberInput name="endYear" label="Year" control={control} min={2020} max={moment().year()} disableEmpty {...(yearDisable ? { disabled: true, value: startYear } : {})} />
                {visibleMonth && <OptionNumberInput name="endMonth" label="Month" control={control} min={1} max={12} {...(monthDisable ? { disabled: true, value: startMonth } : {})} />}
                {visibleDay && <OptionNumberInput name="endDay" label="Day" control={control} min={1} max={30} {...(dayDisable ? { disabled: true, value: startDay } : {})} />}
                {visibleHour && <OptionNumberInput name="endHour" label="Hour" control={control} min={0} max={23} {...(hourDisable ? { disabled: true, value: startHour } : {})} />}
                {visibleMinute && <OptionNumberInput name="endMinute" label="Minute" control={control} min={0} max={59} />}
            </div>

            {/* { watchStartDate && <FormInputWithController name='endDate' labelName='End Date' control={control} type="datetime-local" /> } */}

        </>
    )
}