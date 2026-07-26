"use client";
import { cn } from "@/lib/utils/tw";
import React from "react";
import { Avatar, AvatarImage, AvatarFallback } from "../ui/avatar";
import { useCurrentUser } from "@/lib/hooks/user/useCurrentUser";
import { DeviceRotateIcon, ExclamationMarkIcon } from "@phosphor-icons/react";

const User = () => {
  const {
    data: user,
    isPending,
    isError,
    isSuccess,
    refetch,
  } = useCurrentUser();
  return (
    <div className={cn("w-full max-w-3xs flex gap-2 items-center")}>
      {isPending && (
        <Avatar>
          <DeviceRotateIcon />
        </Avatar>
      )}
      {isError && (
        <Avatar>
          <ExclamationMarkIcon />
        </Avatar>
      )}
      {isSuccess && (
        <>
          <Avatar>
            <AvatarImage src={user.avatarUrl} alt={user.displayName} />
            <AvatarFallback>{user.displayName.split(" ")[0]}</AvatarFallback>
          </Avatar>
          <span>{user.displayName}</span>
        </>
      )}
    </div>
  );
};

export default User;
