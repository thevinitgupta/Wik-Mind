import React from 'react'

function GradientDotGrid() {
  return (
    <div className="absolute inset-0 -z-10">
  {/* Background */}
  <div className="absolute inset-0 bg-background" />

  {/* Radial Gradient */}
  <div
    className="
      absolute inset-0
      bg-[radial-gradient(circle_at_top,rgba(132,204,22,0.12),transparent_60%)]
    "
  />

<div
    className="
        absolute inset-0
        bg-[linear-gradient(rgba(255,255,255,.03)_1px,transparent_1px),
            linear-gradient(90deg,rgba(255,255,255,.03)_1px,transparent_1px)]
        bg-size-[48px_48px]
    "
/>
</div>
  )
}

export default GradientDotGrid