import moment from 'moment';

type Props = {
  data: {
    count: number;
    date: string;
  }[];
  start: string;
  end: string;
};

export type Res = {
  labels: string[];
  data: number[];
};

export default function getLineChartReadable({ start, end, data }: Props): Res {
  const dateFormat = 'YYYY-MM-DD';
  const startDate = moment(start, dateFormat);
  const endDate = moment(end, dateFormat);
  let iterateDate = startDate;
  const dict: Record<string, number> = data.reduce((res, cur) => {
    return {
      ...res,
      [cur.date]: cur.count
    };
  }, {});
  const res: Res = {
    labels: [],
    data: []
  };
  while (iterateDate.isSameOrBefore(endDate)) {
    const date = iterateDate.format(dateFormat);
    const count = dict[date];
    res.labels.push(date);
    res.data.push(count);
    iterateDate = iterateDate.add(1, "days")
  }

  return res;
}
