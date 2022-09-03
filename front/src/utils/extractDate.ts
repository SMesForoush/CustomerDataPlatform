import humanReadableTime from "./humanReadableTime"

function extractDate(time:string): string {
    return humanReadableTime(time.split("T")[0])
}


export default extractDate