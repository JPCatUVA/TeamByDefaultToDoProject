import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubtaskView } from './subtask-view';

describe('SubtaskView', () => {
  let component: SubtaskView;
  let fixture: ComponentFixture<SubtaskView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubtaskView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubtaskView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
