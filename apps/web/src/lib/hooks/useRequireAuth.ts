import { useEffect } from "react";
import { useCurrentUser } from "./user/useCurrentUser";
import { useRouter } from "next/navigation";

export function useRequireAuth() {
  const query = useCurrentUser();
  const router = useRouter();

  useEffect(() => {
    if (query.isError) {
      router.replace("/");
    }
  }, [query.isError]);

  return query;
}
