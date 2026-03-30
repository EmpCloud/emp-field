import * as React from 'react';
import * as RadioGroupPrimitive from '@radix-ui/react-radio-group';
import { Dot } from 'lucide-react';
import { cn } from '@/lib/utils';

const RadioButton = React.forwardRef(({ className, ...props }, ref) => (
  <RadioGroupPrimitive.Item
    ref={ref}
    className={cn(
      'peer h-3 w-3 2xl:h-5 2xl:w-5 shrink-0 rounded-full border border-primary ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-gradient-to-r from-[#1F3A78] to-[#1F3A78] data-[state=checked]:text-white data-[state=checked]:border-none',
      className
    )}
    {...props}>
    <RadioGroupPrimitive.Indicator
      className={cn('flex items-center justify-center text-current')}>
      <Dot className="h-2 w-2 2xl:h-4 2xl:w-4" />
    </RadioGroupPrimitive.Indicator>
  </RadioGroupPrimitive.Item>
));
RadioButton.displayName = RadioGroupPrimitive.Item.displayName;

export { RadioButton };
