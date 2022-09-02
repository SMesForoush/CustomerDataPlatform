import { ReactNode } from "react";
import { useAuth } from "../services/Auth.context";


export default function WithAuth({ children, withAuth = true }: { children: JSX.Element, withAuth?: boolean }): JSX.Element {
    const [authState] = useAuth();
    if ((withAuth && authState) || (!withAuth && !authState)) {
        return children
    }
    return null
}