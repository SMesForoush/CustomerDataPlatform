import moment from 'moment';

function humanReadableTime(time: string): string {
  return moment(time, 'YYYY-MM-DD').format('MMMM Do YYYY');
}

export default humanReadableTime;
