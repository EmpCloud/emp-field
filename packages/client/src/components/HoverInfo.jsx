import { Button } from '@/components/ui/button';
import {
  HoverCard,
  HoverCardArrow,
  HoverCardContent,
  HoverCardTrigger,
} from '@/components/ui/hover-card';

const HoverInfo = ({ trigger, content }) => {
  return (
    <>
      <HoverCard>
        <HoverCardTrigger asChild>
          <div>{trigger}</div>
        </HoverCardTrigger>
        <HoverCardContent side="right" className="w-fit max-w-52">
          <div className="flex justify-between space-x-4">{content}</div>
          <HoverCardArrow className="HoverCardArrow fill-white drop-shadow" />
        </HoverCardContent>
      </HoverCard>
    </>
  );
};

export default HoverInfo;
