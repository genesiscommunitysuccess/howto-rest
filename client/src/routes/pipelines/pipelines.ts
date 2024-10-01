import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { PipelinesStyles as styles } from './pipelines.styles';
import { PipelinesTemplate as template } from './pipelines.template';

@customElement({
  name: 'pipelines-route',
  template,
  styles,
})
export class Pipelines extends GenesisElement {
  @User user: User;

  constructor() {
    super();
  }
}
