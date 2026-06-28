"use client"
import Login from "@/components/auth/login";
import { Card, CardAction, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import {BrainIcon, CopyrightIcon, LockIcon, DoorOpenIcon} from "@phosphor-icons/react"
import GradientDotGrid from "@/components/textures/gradientDotGrid";
import Aurora from "@/components/textures/aurora";
import { Separator } from "@/components/ui/separator";

export default function Home() {
  return (
    <div className="flex flex-col flex-1 items-center justify-center font-sans">
      <GradientDotGrid/>
      <Aurora/>
      <main className="flex flex-1 w-full flex-col items-center justify-center py-32 px-16 sm:items-start">
      <Card className="h-[50vh] w-full max-w-md mx-auto" size="default">
        <CardHeader>
          <CardTitle className={cn("w-full")}>
            <div className={cn("w-full","flex","gap-4","items-center","justify-center","text-4xl", "font-bold", "tracking-tight")}>
              <BrainIcon className="cn-rtl-flip" />
              WIK Mind
            </div>
            </CardTitle>
          <CardDescription className={cn("w-full","flex","flex-col","gap-2","items-center","justify-center", "text-center")}> 
            <p>Organize knowledge, connect ideas, and remember what you learn.</p>
          </CardDescription>
        </CardHeader>
        <CardContent className={cn("flex-1")}>
          <Login/>
        </CardContent>
        <CardFooter>
          <div className={cn("w-full flex gap-2 justify-center")}>
            <LockIcon className="cn-rtl-flip" />
            <Label>Private by default</Label>
            <Separator orientation="vertical"/>
            <Label>Free to use</Label>
          </div>
        </CardFooter>
      </Card>
      </main>
    </div>
  );
}
