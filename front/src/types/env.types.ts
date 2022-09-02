declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NEXT_PUBLIC_AUTH_COOKIE_NAME: string;
      NEXT_PUBLIC_API_URL: string;
      NEXT_PUBLIC_GRAFANA_URL: string;
    }
  }
}

export {};
