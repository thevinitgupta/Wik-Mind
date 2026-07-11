"use client";
import { useCurrentUser } from '@/lib/hooks/useCurrentUser'
import { useRouter } from 'next/navigation';
import React, { useEffect } from 'react'

const Dashboard = () => {
  const router = useRouter();
  const {data : user, isError, isPending} = useCurrentUser();

  useEffect(() => {
    if (isError) {
      router.replace("/");
    }
  }, [isError, router]);

  if(isPending) {
    return <div>Fetching User Details...</div>
  }
  return (
    <div>
      <h2>Logged In as : </h2>
      {
        user?.displayName
      }
    </div>
  )
}

export default Dashboard