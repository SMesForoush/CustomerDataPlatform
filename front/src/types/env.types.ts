declare global {
  namespace NodeJS {
    interface ProcessEnv {
      AUTH_COOKIE_NAME: string;
      NEXT_PUBLIC_API_URL: string;
    }
  }
}

export {};
