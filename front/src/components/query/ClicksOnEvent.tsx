import { QueryComponentProps } from "../../pages/dashboard/charts";

export default function ClicksOnEvent({startDate, endDate}: QueryComponentProps): JSX.Element {

    return (
        <button type="button" value="query" >Submit </button>
    )
}