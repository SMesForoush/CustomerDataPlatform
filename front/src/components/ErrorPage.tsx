
export default function ErrorPage({ message }: { message: string }): JSX.Element {
  return <div className="border border-red-500 p-10 text-3xl">{message}</div>;
}
