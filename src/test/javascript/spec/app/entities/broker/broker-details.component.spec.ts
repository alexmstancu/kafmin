/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import BrokerDetailComponent from '@/entities/broker/broker-details.vue';
import BrokerClass from '@/entities/broker/broker-details.component';
import BrokerService from '@/entities/broker/broker.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Broker Management Detail Component', () => {
    let wrapper: Wrapper<BrokerClass>;
    let comp: BrokerClass;
    let brokerServiceStub: SinonStubbedInstance<BrokerService>;

    beforeEach(() => {
      brokerServiceStub = sinon.createStubInstance<BrokerService>(BrokerService);

      wrapper = shallowMount<BrokerClass>(BrokerDetailComponent, { store, localVue, provide: { brokerService: () => brokerServiceStub } });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBroker = { id: 123 };
        brokerServiceStub.find.resolves(foundBroker);

        // WHEN
        comp.retrieveBroker('dummyCLusterId', 123);
        await comp.$nextTick();

        // THEN
        expect(comp.broker).toBe(foundBroker);
      });
    });
  });
});
