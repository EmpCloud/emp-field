import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

export function StageModal({
  disable,
  triggerText,
  triggerIcon,
  triggerButtonIcon,
  title,
  titleStyles,
  buttonStyles,
  children,
}) {
  return (
    <Dialog className="m-5">
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
        className="border-0 w-[550px]"
        onWheel={e => e.stopPropagation()}>
        <DialogHeader className="col-span-12">
          <DialogTitle className={titleStyles}>{title}</DialogTitle>
        </DialogHeader>
        {children}
      </DialogContent>
    </Dialog>
  );
}
