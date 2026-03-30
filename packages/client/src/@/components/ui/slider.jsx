import * as React from 'react';
import * as SliderPrimitive from '@radix-ui/react-slider';
import { cn } from '@/lib/utils';

const Slider = React.forwardRef(({ className, frequency, ...props }, ref) => {
  const [thumbPosition, setThumbPosition] = React.useState({ x: 0, y: 0 });
  const [isThumbActive, setIsThumbActive] = React.useState(false);

  const handleThumbMove = event => {
    const thumbRect = event.target.getBoundingClientRect();
    const thumbCenterX = thumbRect.left + thumbRect.width / 2;
    const thumbTopY = thumbRect.top;
    setThumbPosition({ x: thumbCenterX, y: thumbTopY });
  };

  return (
    <SliderPrimitive.Root
      ref={ref}
      className={cn(
        'relative flex w-full touch-none select-none items-center',
        className
      )}
      {...props}>
      <SliderPrimitive.Track className="relative h-2 w-full grow overflow-hidden rounded-full bg-gray-400/10">
        <SliderPrimitive.Range className="absolute h-full bg-gradient-to-r from-violet-600 to-blue-500" />
      </SliderPrimitive.Track>
      <SliderPrimitive.Thumb
        className={cn(
          'block h-4 2xl:h-5 w-4 2xl:w-5 rounded-full cursor-pointer bg-gradient-to-r from-violet-600 to-blue-500 ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50',
          'hover:bg-blue-600 active:bg-blue-700'
        )}
        onPointerMove={handleThumbMove}
        onFocus={() => setIsThumbActive(true)}
        onBlur={() => setIsThumbActive(false)}
        onMouseEnter={() => setIsThumbActive(true)}
        onMouseLeave={() => setIsThumbActive(false)}>
        {typeof frequency === 'number' && isThumbActive && (
          <div
            className={cn(
              'absolute -top-8 left-1/2 transform -translate-x-1/2 text-xs text-gray-500 bg-white shadow rounded px-2 py-1',
              'transition-all duration-300',
              thumbPosition.x !== 0 && 'translate-x-[-50%]'
            )}>
            <span>{frequency}</span>
          </div>
        )}
      </SliderPrimitive.Thumb>
    </SliderPrimitive.Root>
  );
});

Slider.displayName = SliderPrimitive.Root.displayName;

export { Slider };
