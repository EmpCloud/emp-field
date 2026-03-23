import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { useLocation } from 'react-router-dom';

export function Modal({
  disable,
  triggerText,
  triggerIcon,
  triggerButtonIcon,
  title,
  titleStyles,
  buttonStyles,
  children,
  largeWidth,
  open,
  onOpenChange,
}) {
  const location = useLocation();
  return (
    <Dialog className={`m-5 `} open={open} onOpenChange={onOpenChange}>
      <DialogTrigger asChild>
        {triggerIcon ? (
          <div>{triggerIcon}</div>
        ) : (
          <Button disabled={disable} className={buttonStyles}>
            {triggerButtonIcon}
            {triggerText}
          </Button>
        )}
      </DialogTrigger>
      <DialogContent
        className={`border-0 ${location.pathname === '/admin/task' || location.pathname === '/admin/clients' ? '!w-58per' : ''} ${largeWidth}`}
        onWheel={e => e.stopPropagation()}>
        <DialogHeader className="col-span-12">
          <DialogTitle className={titleStyles}>{title}</DialogTitle>
        </DialogHeader>
        {children}
      </DialogContent>
    </Dialog>
  );
}
