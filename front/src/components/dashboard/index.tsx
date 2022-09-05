import Link from "next/link";

export function Dashboard(): JSX.Element {
  return (
    <>
      <head>
        <meta
          http-equiv="set-cookie"
          content="Value=_ga, Site=.grafana.net/; SameSite=None; Secure;"
        />
        <meta
          http-equiv="set-cookie"
          content="Value=_gid, Site=.grafana.net/; SameSite=None; Secure;"
        />
      </head>
      <div className="flex flex-col">
        <ul className="text-blue-600 space-y-4">
          <li>
            <Link href="/dashboard/grafana">
              See grafana dashboard
            </Link>
	  </li>
	  <li>
            <Link href="/dashboard/charts">
              See offline query dashboard
            </Link>
          </li>
        </ul>
      </div>
    </>
  );
}
