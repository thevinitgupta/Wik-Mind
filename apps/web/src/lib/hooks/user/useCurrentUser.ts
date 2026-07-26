import { useQuery } from "@tanstack/react-query";

import { getCurrentUser } from "@/service/auth.service";

export function useCurrentUser() {
    return useQuery({
        queryKey: ["me"],
        queryFn: getCurrentUser,
        retry : false
    });
}