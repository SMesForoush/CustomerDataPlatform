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
        <ul className="text-blue-600">
          <li>
            <a href={`${process.env.NEXT_PUBLIC_GRAFANA_URL}`} target="_blank">
              Go to grafana page
            </a>
          </li>
        </ul>
      </div>
      {/* <iframe
        src={`${process.env.NEXT_PUBLIC_GRAFANA_URL}/d/kBjS2mG4z/test-2?orgId=1&from=now-90d&to=now&var-org_user=viewer&kiosk=tv`}
      /> */}
      {/* <iframe width="100%" height={1000} src={`${process.env.NEXT_PUBLIC_GRAFANA_URL}`} /> */}
    </>
  );
}
