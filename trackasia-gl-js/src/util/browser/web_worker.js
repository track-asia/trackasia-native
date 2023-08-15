// @flow

import window from '../window';
import TrackAsiaGL from '../../';

import type {WorkerInterface} from '../web_worker';

export default function (): WorkerInterface {
    return (new window.Worker(TrackAsiaGL.workerUrl): any);
}
