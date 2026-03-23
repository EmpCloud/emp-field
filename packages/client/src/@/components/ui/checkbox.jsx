import * as React from 'react';
import * as CheckboxPrimitive from '@radix-ui/react-checkbox';
import { Check } from 'lucide-react';

import { cn } from '@/lib/utils';

const Checkbox = React.forwardRef(
  ({ className, checkClass, chckColor, ...props }, ref) => (
    <CheckboxPrimitive.Root
      ref={ref}
      className={cn(
        'peer h-3 w-3 2xl:h-5 2xl:w-5 shrink-0 rounded-[2px] border border-primary ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-gradient-to-r from-[#1F3A78] to-[#1F3A78] data-[state=checked]:text-white data-[state=checked]:border-none',
        className,
        chckColor
      )}
      {...props}>
      <CheckboxPrimitive.Indicator
        className={cn('flex items-center justify-center text-current')}>
        <Check className={`h-2 w-2 2xl:h-4 2xl:w-4 ${checkClass}`} />
      </CheckboxPrimitive.Indicator>
    </CheckboxPrimitive.Root>
  )
);
Checkbox.displayName = CheckboxPrimitive.Root.displayName;

export { Checkbox };
