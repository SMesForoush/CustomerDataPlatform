import React from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Pie } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);
const bgColors = [
    'rgba(255, 99, 132, 0.2)',
    'rgba(54, 162, 235, 0.2)',
    'rgba(255, 206, 86, 0.2)',
    'rgba(75, 192, 192, 0.2)',
    'rgba(153, 102, 255, 0.2)',
    'rgba(255, 159, 64, 0.2)',
]

const borderColor = [
    'rgba(255, 99, 132, 1)',
    'rgba(54, 162, 235, 1)',
    'rgba(255, 206, 86, 1)',
    'rgba(75, 192, 192, 1)',
    'rgba(153, 102, 255, 1)',
    'rgba(255, 159, 64, 1)',
]

function getRandom(array: string[]): string {
    return array[Math.floor(Math.random() * array.length)]
}
function createData(labels: string[], data: number[]) {
    return {
        labels: labels,
        datasets: [
            {
                label: '# of Clicks',
                data: data,
                backgroundColor: data.map(_ => getRandom(bgColors)),
                borderColor: data.map(_ => getRandom(borderColor)),
                borderWidth: 1,
            },
        ],
    };
}

type PieChartProps = {
    data: number[];
    labels: string[];

}
export function PieChart({ data: chartData, labels }: PieChartProps) {
    const data = createData(labels, chartData);
    return <Pie data={data} />;
}
