"use client";
import React from 'react'
import { Button } from "@/components/ui/button"
import {GoogleLogoIcon, GithubLogoIcon} from "@phosphor-icons/react"
import { cn } from '@/lib/utils';

function Login() {
  return (
    <div className={cn("flex", "flex-col", "gap-4", "items-center", "justify-center")}>
    <Button variant="secondary"  type='button'>
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