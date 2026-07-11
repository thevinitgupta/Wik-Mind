"use client";
import React from 'react'
import { Button } from "@/components/ui/button"
import {GoogleLogoIcon, GithubLogoIcon} from "@phosphor-icons/react"
import { cn } from '@/lib/utils';
import httpClient from '@/lib/httpClient';

function Login() {
  const handleAuthWithGoogle = () => {
    window.location.href =
        "http://localhost:8080/oauth2/authorization/google";
  }
  return (
    <div className={cn("flex", "flex-col", "gap-4", "items-center", "justify-center")}>
    <Button onClick={handleAuthWithGoogle} variant="secondary"  type='button'>
        <GoogleLogoIcon data-icon="inline-start"/>
        Continue with Google
    </Button>
    <Button variant="outline" type='button'>
        <GithubLogoIcon data-icon="inline-start"/>
        Continue with Github
    </Button>

    </div>
  )
}

export default Login