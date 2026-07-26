"use client"
import { SpinnerGapIcon } from '@phosphor-icons/react'

export default function Loader ({className}: {className? : string}) {
  return (
    <div className='w-full h-full min-h-20 flex justify-center items-center'>
        <SpinnerGapIcon  className={`animate-spin cn-rtl-flip ${className}`} />
    </div>
  )
}
