import moment from 'moment';
import { QueryComponentProps } from '../pages/dashboard/charts';

type Props = {
  data: {
    count: number;
    date: number;
  }[];
  date: QueryComponentProps;
};

export type Res = {
  labels: string[];
  data: number[];
};

enum ChartType {
  year = 'y',
  month = 'M',
  day = 'd',
  hour = 'h',
  minute = 'm'
}
function getChartType(date: Props['date']) {
  if (date.endYear !== date.startYear) return ChartType.year;
  else if (date.endMonth !== date.startMonth) return ChartType.month;
  else if (date.endDay !== date.startDay) return ChartType.day;
  else if (date.endHour !== date.startHour) return ChartType.hour;
  return ChartType.minute;
}

const startChartMapping: Record<ChartType, keyof Props['date']> = {
  d: 'startDay',
  h: 'startHour',
  M: 'startMonth',
  m: 'startMinute',
  y: 'startYear'
};

const endChartMapping: Record<ChartType, keyof Props['date']> = {
  d: 'endDay',
  h: 'endHour',
  M: 'endMonth',
  m: 'endMinute',
  y: 'endYear'
};

export default function getLineChartReadable({ date, data }: Props): Res {
  const chartType = getChartType(date);

  //   const dateFormat = 'YYYY-MM-DD';
  //   const startDate = moment(start, dateFormat);
  //   const endDate = moment(end, dateFormat);
  const start = date[startChartMapping[chartType]];
  const end = date[endChartMapping[chartType]];
  let iterateDate = start;
  const dict: Record<number, number> = data.reduce((res, cur) => {
    return {
      ...res,
      [cur.date]: cur.count
    };
  }, {});
  const res: Res = {
    labels: [],
    data: []
  };
  while (iterateDate <= end) {
    const count = dict[iterateDate];
    res.labels.push(iterateDate.toString() || '0');
    res.data.push(count);
    iterateDate = iterateDate + 1;
  }

  return res;
}
