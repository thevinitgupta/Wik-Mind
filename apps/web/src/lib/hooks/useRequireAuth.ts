import { useEffect } from "react";
import { useCurrentUser } from "./useCurrentUser";
import { useRouter } from "next/navigation";

export function useRequireAuth() {

    const query = useCurrentUser();
    const router = useRouter()

    useEffect(() => {
        if (query.isError) {
            router.replace("/login");
        }
    }, [query.isError]);

    return query;

}