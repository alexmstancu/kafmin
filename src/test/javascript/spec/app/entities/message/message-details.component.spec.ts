/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import MessageDetailComponent from '@/entities/message/message-details.vue';
import MessageClass from '@/entities/message/message-details.component';
import MessageService from '@/entities/message/message.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Message Management Detail Component', () => {
    let wrapper: Wrapper<MessageClass>;
    let comp: MessageClass;
    let messageServiceStub: SinonStubbedInstance<MessageService>;

    beforeEach(() => {
      messageServiceStub = sinon.createStubInstance<MessageService>(MessageService);

      wrapper = shallowMount<MessageClass>(MessageDetailComponent, {
        store,
        localVue,
        provide: { messageService: () => messageServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMessage = { id: 123 };
        messageServiceStub.find.resolves(foundMessage);

        // WHEN
        comp.retrieveMessage(123);
        await comp.$nextTick();

        // THEN
        expect(comp.message).toBe(foundMessage);
      });
    });
  });
});
