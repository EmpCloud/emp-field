import React from 'react';
import { Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';

const ButtonLoading = ({ disableButton, isLoading, children, ...rest }) => {
  return (
    <Button {...rest} disabled={isLoading || disableButton}>
      {isLoading ? (
        <>
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
          Please wait
        </>
      ) : (
        children
      )}
    </Button>
  );
};

export default ButtonLoading;
