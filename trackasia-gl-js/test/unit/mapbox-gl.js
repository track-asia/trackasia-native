import {test} from '../util/test';
import TrackAsiaGL from '../../src';

test('TrackAsiaGL', (t) => {
    t.test('version', (t) => {
        t.ok(TrackAsiaGL.version);
        t.end();
    });

    t.test('workerCount', (t) => {
        t.ok(typeof TrackAsiaGL.workerCount === 'number');
        t.end();
    });
    t.end();
});
