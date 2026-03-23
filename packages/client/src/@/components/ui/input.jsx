import * as React from 'react';

import { cn } from '@/lib/utils';

const Input = React.forwardRef(
  ({ className, defaultValue, type, ...props }, ref) => {
    return (
      <input
        defaultValue={defaultValue}
        type={type}
        className={cn(
          'flex h-6 2xl:h-10 w-full rounded-md bg-background ps-0 pe-3 text-[10px] 2xl:text-sm font-semibold ring-offset-background file:border-0 file:bg-transparent file:text-sm placeholder:text-primary placeholder:font-medium focus-visible:outline-none disabled:cursor-not-allowed disabled:opacity-50 text-primary',
          className
        )}
        ref={ref}
        {...props}
      />
    );
  }
);
Input.displayName = 'Input';

export { Input };
