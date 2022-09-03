import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    ChartOptions,
    ChartData,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

type LineChartProps = {
    data: number[];
    labels: string[];

}

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);
const options: ChartOptions<'line'> = {
    responsive: true,
    interaction: {
        mode: 'index' as const,
        intersect: false,
    },
    plugins: {
        title: {
            display: true,
            text: 'Query Result',
        },
    },
    scales: {
        y: {
            type: 'linear' as const,
            display: true,
            position: 'left' as const,
        },
        // y1: {
        //     type: 'linear' as const,
        //     display: true,
        //     position: 'left' as const,
        //     grid: {
        //         drawOnChartArea: false,
        //     },
        // },
    },
}

export default function LineChart({ data, labels }: LineChartProps): JSX.Element {
    const chartData: ChartData<'line', LineChartProps['data'], LineChartProps['labels'][0]> =
    {
        labels,
        datasets: [
            {
                label: 'Result',
                data,
                borderColor: 'rgb(255, 99, 132)',
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                yAxisID: 'y',
            },
            // {
            //     label: 'Dataset 2',
            //     data: labels.map(() => Math.random() * 100),
            //     borderColor: 'rgb(53, 162, 235)',
            //     backgroundColor: 'rgba(53, 162, 235, 0.5)',
            //     yAxisID: 'y1',
            // },
        ],
    };

    return (
        <Line options={options} data={chartData} />
    )
}