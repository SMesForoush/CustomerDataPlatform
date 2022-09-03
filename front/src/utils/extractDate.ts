import humanReadableTime from "./humanReadableTime"

function extractDate(time:string): string {
    return time.split("T")[0]
}


export default extractDate